import React, { Component } from 'react';
import Redirect from 'react-router';

class Home extends Component {

    render() {

        return (
            <div>
                <Redirect to="/pooler/landing" />
            </div>
        )
    }
}
//export Home Component
export default Home;