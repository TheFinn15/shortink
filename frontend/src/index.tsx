import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import HomePage from './HomePage';
import UserPage from './UserPage';
import Toolbar from './ToolbarComp'
import reportWebVitals from './reportWebVitals';
import {BrowserRouter, Route, Switch} from "react-router-dom";

ReactDOM.render(
  <React.StrictMode>
    <BrowserRouter>
      <Toolbar/>
      <Switch>
        <Route exact path="/">
          <HomePage/>
        </Route>
        <Route path="/cabinet">
          <UserPage/>
        </Route>
      </Switch>
    </BrowserRouter>
  </React.StrictMode>,
  document.getElementById('root')
);

reportWebVitals();
