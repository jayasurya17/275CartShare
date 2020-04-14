import React, { Component } from 'react';
import Header from '../Common/header';
import Navigation from '../Common/navbar';

class Home extends Component {

    render() {

        return (
            <div>
                <Header />
                <Navigation />
                <div className="pl-5 pr-5">
                    
                    {/* Component design goes here */}

                </div>
            </div>
        )
    }
}
//export Home Component
export default Home;