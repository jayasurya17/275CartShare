import React, { Component } from 'react';
import { Route } from 'react-router-dom';
import Landing from './Authentication/landing';
import Login from './Authentication/login';
import VerifyUser from './Authentication/verify';


import PoolerLanding from './Pooler/Landing/landing';
import PoolerBrowseStores from './Pooler/browseStores';
import PoolerSearch from './Pooler/search';
import PoolerUpdateAccount from './Pooler/updateAccount';

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
                <Route path="/pooler/browse" component={PoolerBrowseStores} />
                <Route path="/pooler/search" component={PoolerSearch} />
                <Route path="/pooler/update/account" component={PoolerUpdateAccount} />
            </div>
        )
    }
}
//Export The Main Component
export default Main;