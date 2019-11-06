'use strict';

// NOTE: Normally I would put each class in its own file, then bundle them together.
// Private functions start with an underscore.

/**
 * Static class that creates a chat message HTMLElement
 */
class Message {
    static _createNewMessage() {
        return document.createElement('div');
    }

    static _createMessageBody(message) {
        const messageBody = document.createElement('span');
        messageBody.classList.add('typography-normal');
        messageBody.textContent = message.message;
        return messageBody;
    }

    static _createMessageUser(message) {
        const messageUser = document.createElement('span');
        messageUser.classList.add('typography-bold');
        messageUser.textContent = `${message.user}: `;
        return messageUser;
    }

    /**
    * Returns a chat message where the user is not present and the content is aligned right
    */
    static createMyMessage(message) {
        const newMessage = Message._createNewMessage();

        newMessage.classList.add('chat__message--own')
        newMessage.appendChild(Message._createMessageUser(message));
        newMessage.appendChild(Message._createMessageBody(message));

        return newMessage;
    }

    /**
    * Returns a standard chat message with the user and message body
    */
    static createRemoteMessage(message) {
        const newMessage = Message._createNewMessage();

        newMessage.appendChild(Message._createMessageUser(message));
        newMessage.appendChild(Message._createMessageBody(message));

        return newMessage;
    }
}

class ElementNotFoundError extends Error { };

/**
* This class handles the element selection for the chat form. Currently only works if there is exactly one chat form in the DOM.
* Throws ElementNotFoundError if any chat element is not found.
*/
class ChatForm {
    _getFirstElementByClassName(className) {
        let potentialElement = document.getElementsByClassName(className)[0];
        if (!potentialElement) {
            throw new ElementNotFoundError(`Element not found with class name: ${className}`);
        }
        return potentialElement;
    }

    constructor() {
        this.userInput = this._getFirstElementByClassName('chat__inputs__user');
        this.messageInput = this._getFirstElementByClassName('chat__inputs__message');
        this.sendButton = this._getFirstElementByClassName('chat__inputs__send');
        this.messagesDisplay = this._getFirstElementByClassName('chat__messages');
    }
}

/**
* Handles rendering of the messages in the chat form.
*/
class MessageRenderer {
    constructor(chatForm) {
        this.chatForm = chatForm;
    }

    _appendMessage(messageElement) {
        this.chatForm.messagesDisplay.appendChild(messageElement);
    }

    /**
    * After each message is added, the chat window has to be scrolled to the bottom manually.
    */
    _scrollToBottom() {
        this.chatForm.messagesDisplay.scrollTop = this.chatForm.messagesDisplay.scrollHeight - this.chatForm.messagesDisplay.clientHeight;
    }

    appendMyMessage(message) {
        this._appendMessage(Message.createMyMessage(message));
        this._scrollToBottom();
    }

    appendRemoteMessage(message) {
        this._appendMessage(Message.createRemoteMessage(message));
        this._scrollToBottom();
    }
}

/**
* Handles sending of the messages in the chat form.
*/
class MessageSender {
    constructor(serverendpoint, chatForm, messageRenderer, botId) {
        this.chatForm = chatForm;
        this.messageRenderer = messageRenderer;
        this.serverendpoint = serverendpoint;
        this.botId = botId;
        this.chatContextId = this.generateChatId();
    }
    generateChatId() {
        function s4() {
            return Math.floor((1 + Math.random()) * 0x10000)
                .toString(16)
                .substring(1);
        }
        return s4() + s4() + "-" + s4() + "-" + s4() + "-" + s4() + "-" + s4() + s4() + s4();
    }

    /**
    * Sends the message via rest and resets the form's value if there is a message present and returns serverresponse
    */
    sendMessage() {
        let message = this.chatForm.messageInput.value;
        let completeUrl = `${this.serverendpoint}/chat?message=${message}&chatContextId=${this.chatContextId}&botId=${this.botId}`;
        if (message) {
            let user = this.chatForm.userInput.value || 'Guest';
            let messageObj = { user, message };

            this.messageRenderer.appendMyMessage(messageObj);
            this.chatForm.messageInput.value = '';
            var xmlHttp = new XMLHttpRequest();
            xmlHttp.open("GET", completeUrl, false); 
            xmlHttp.send(null);
            let serverResponse=JSON.parse(xmlHttp.response);
            serverResponse.forEach(responseText => {
                this.messageRenderer.appendRemoteMessage({ "user": "Bot", "message": responseText });

            });
        }
    }

    sendInitializingMessage() {
        let completeUrl = `${this.serverendpoint}/chat?message=&chatContextId=${this.chatContextId}&botId=${this.botId}`;
        var xmlHttp = new XMLHttpRequest();
            xmlHttp.open("GET", completeUrl, false); // false for synchronous request
            xmlHttp.send(null);
            let serverResponse=JSON.parse(xmlHttp.response);
            serverResponse.forEach(responseText => {
                this.messageRenderer.appendRemoteMessage({ "user": "Bot", "message": responseText });

            });
            return xmlHttp.responseText;
    }

    /**
    * Sends the message on pressing the enter key in the message input
    */
    sendMessageOnEnter(event) {
        if (event.keyCode == 13) {
            this.chatForm.sendButton.click();
            this.chatForm.sendButton.classList.add('chat__button--active');
            setTimeout(() => {
                this.chatForm.sendButton.classList.remove('chat__button--active');
            }, 100);
            this.sendMessage();
        }
    }
}

/**
* The application uses the CHAT namespace on window.
*/


window.CHAT = {};
CHAT.chatForm = new ChatForm();
CHAT.messageRenderer = new MessageRenderer(CHAT.chatForm);
CHAT.messageSender = new MessageSender("http://localhost:8080", CHAT.chatForm, CHAT.messageRenderer, "praxisbericht_bot");
CHAT.messageSender.sendInitializingMessage();
