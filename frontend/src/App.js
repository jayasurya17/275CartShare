import React, { Component } from 'react';
import './App.css';
import Main from './components/Main';
import {BrowserRouter} from 'react-router-dom';
import firebase from 'firebase';
import axios from 'axios';

firebase.initializeApp({
  apiKey: 'AIzaSyCJQQyxT7Juillp8CoJ_0ayFUDrYMfm58A',
  authDomain: 'cartshare-29f90.firebaseapp.com'
})

// axios.defaults.baseURL = 'http://localhost:5000';
axios.defaults.baseURL = 'http://Cartsharebackend-env.eba-yapjdser.us-west-1.elasticbeanstalk.com';

//App Component
class App extends Component {


  render() {
    return (
      //Use Browser Router to route to different pages
      <BrowserRouter>
        <div>
          {/* App Component Has a Child Component called Main*/}
          <Main/>
        </div>
      </BrowserRouter>
    );
  }
}
//Export the App component so that it can be used in index.js
export default App;
