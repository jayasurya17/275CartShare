import React, { Component } from 'react';
import Header from '../../Common/header';
import CreatePool from './createPool';
import BrowsePools from './browsePools';

class Home extends Component {

    render() {

        return (
            <div>
                <Header isLanding={true} />
                <p className="display-4 text-center pt-5">What would you like to do?</p>
                <div className="row">
                    <div className="col-md-6 border-right pl-5 pr-5">
                        <CreatePool />
                    </div>
                    <div className="col-md-6 border-left pl-5 pr-5">
                        <BrowsePools />
                    </div>
                </div>
            </div>
        )
    }
}
//export Home Component
export default Home;