function log(msg) {
  const messagesDiv = document.getElementById('messages');
  const elem = document.createElement('div');
  const txt = document.createTextNode(msg);
  elem.appendChild(txt);
  messagesDiv.append(elem);
}

window.addEventListener('load', function (e) {
  log("window has loaded.");
  const eventSource = new EventSource('http://localhost:8080/sse/10');
  eventSource.addEventListener('message', function (e) {
    e.preventDefault();
    log(e.data);
  });
  eventSource.addEventListener('error', function (e) {
    e.preventDefault();
    log('closing the EventSource...')
    eventSource.close();
  });
});

//Excerpt From: Josh Long. “Reactive Spring”.