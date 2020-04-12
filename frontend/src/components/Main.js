import React, { Component } from 'react';
import { Route } from 'react-router-dom';
import Landing from './Authentication/landing';
import Login from './Authentication/login';
import VerifyUser from './Authentication/verify';


import PoolerLanding from './Pooler/landing';

//Create a Main Component
class Main extends Component {
    render() {
        return (
            <div>
                {/*Render Different Component based on Route*/}
                <Route path="/" component={Landing} />
                <Route path="/login" component={Login} />
                <Route path="/verify" component={VerifyUser} />


                <Route path="/pooler/landing" component={PoolerLanding} />
            </div>
        )
    }
}
//Export The Main Component
export default Main;