// Dimensions of the photo to be taken
var width = 320;    // We will scale the photo width to this
var height = 0;     // This will be computed based on the input stream

// Whether we are currently streaming
var streaming = false;

// Global HTML elements
var video;
var canvas;
var photo;
var startButton;

function init() {
  video = document.getElementById('video');
  canvas = document.getElementById('canvas');
  photo = document.getElementById('photo');
  startButton = document.getElementById('startbutton');

  navigator.mediaDevices.getUserMedia({
    video: true, 
    audio: false
  })
  .then(function(stream) {
    video.srcObject = stream;
    video.play();
  })
  .catch(function(err) {
    console.log("An error occurred: " + err);
  });

  video.addEventListener('canplay', function() {
    if (!streaming) {
      // Calculate appropriate height based off of desired width and given video dimensions
      height = video.videoHeight / (video.videoWidth / width);
      if (isNaN(height)) {
        height = width / (4 / 3);
      }
    
      video.setAttribute('width', width);
      video.setAttribute('height', height);
      canvas.setAttribute('width', width);
      canvas.setAttribute('height', height);

      streaming = true;
    }
  }, false);

  startButton.addEventListener('click', function(e){
    takePicture();
    e.preventDefault();
  }, false);
  
  clearCanvas();
}

// Fill the photo with an indication that none has been
// captured.

function clearCanvas() {
  var context = canvas.getContext('2d');
  context.fillStyle = "#AAA";
  context.fillRect(0, 0, canvas.width, canvas.height);

  var data = canvas.toDataURL('image/png');
  photo.setAttribute('src', data);
}

// Capture a photo by fetching the current contents of the video
// and drawing it into a canvas, then converting that to a PNG
// format data URL. By drawing it on an offscreen canvas and then
// drawing that to the screen, we can change its size and/or apply
// other changes before drawing it.

function takePicture() {
  var context = canvas.getContext('2d');
  if (width && height) {
    canvas.width = width;
    canvas.height = height;
    context.drawImage(video, 0, 0, width, height);

    var data = canvas.toDataURL('image/png');
    photo.setAttribute('src', data);
    uploadImage(data);
  } else {
    clearCanvas();
  }
}

const backend_host = '';
function uploadImage(data) {
  fetch(backend_host + '/images', {
    method: 'POST',
    body: data
  })
    .then(result => {
      console.log('Success:', result);
    })
    .catch(error => {
      console.error('Error:', error);
    });
}

function validate() {
  var context = canvas.getContext('2d');
  canvas.width = width;
  canvas.height = height;
  context.drawImage(video, 0, 0, width, height);

  var data = canvas.toDataURL('image/png');
  photo.setAttribute('src', data);

  fetch(backend_host + '/validate', {
    method: 'POST',
    body: data
  })
    .then(response => response.json())
    .then(data => {
      result = document.getElementById('result');
      result.innerHTML = "";
      for (var i = 0; i < data.predictions.length; i++) {
        console.log("Tag Name: " + data.predictions[i].tagName + " Probability: " + data.predictions[i].probability);
        result.innerHTML += "Tag Name: " + data.predictions[i].tagName + " Probability: " + data.predictions[i].probability + "<br>";
      }
    })
    .catch(error => {
      console.error('Error:', error);
    });
}

function submitForm(event) {
  event.preventDefault();
  var context = canvas.getContext('2d');
  canvas.width = width;
  canvas.height = height;
  context.drawImage(video, 0, 0, width, height);

  var data = canvas.toDataURL('image/png');
  photo.setAttribute('src', data);

  var name = document.getElementById("fullname").value;
  var email = document.getElementById("email").value;

  fetch(backend_host + '/form', {
    method: 'POST',
    body: JSON.stringify({
      name: name, 
      email: email, 
      image: data
    })
  })
    .then(result => {
      console.log('Success:', result);
    })
    .catch(error => {
      console.error('Error:', error);
    });
}

// Run startup procedure on window load
window.addEventListener('load', init, false);
