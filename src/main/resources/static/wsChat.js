window.addEventListener('load', function (e) {
  const messages = document.getElementById('messages');
  const button = document.getElementById('send');
  const message = document.getElementById('message');
  const websocket = new WebSocket('ws://localhost:8080/ws/chat');

  websocket.addEventListener('message', function (e) {
    const messageJson = JSON.parse(e.data);
    console.dir(messageJson);
    const card = document.createElement('div');
    card.className = 'card blue-text text-darken-2'
    const cardContent = document.createElement('div');
    cardContent.className = 'card-content blue-text text-darken-2';
    card.append(cardContent);
    const cardTitle = document.createElement('span');
    cardTitle.className = 'card-title';
    cardTitle.innerText = `From ${messageJson.clientId} at ${format(messageJson.when)}`;
    cardContent.append(cardTitle);
    const text = document.createElement('p');
    text.innerText = messageJson.text;
    cardContent.append(text);
    messages.appendChild(card);
  });

  function format(date) {
    return dayjs(date).format('LL LTS');
  }

  function send() {
    const value = message.value;
    message.value = '';
    websocket.send(JSON.stringify({'text': value.trim()}));
  }

  message.addEventListener('keydown', function (e) {
    if (e.key === 'Enter') {
      send();
    }
  });

  button.addEventListener('click', function (e) {
    send();
    e.preventDefault();
    return false;
  });
});

//Excerpt From: Josh Long. “Reactive Spring”.
