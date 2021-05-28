function log(msg) {
  const messagesDiv = document.getElementById('messages');
  const elem = document.createElement('div');
  const txt = document.createTextNode(msg);
  elem.appendChild(txt);
  messagesDiv.append(elem);
}

let websocket = null;

document
    .getElementById('close')
    .addEventListener('click', function (evt) {
      evt.preventDefault();
      websocket.close();
      return false;
    });

window.addEventListener('load', function (e) {
  websocket = new WebSocket('ws://localhost:8080/ws/echo');
  websocket.addEventListener('message', function (e) {
    websocket.send(e.data + ' reply');
  });
});

//Excerpt From: Josh Long. “Reactive Spring”.