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
                    <h1 class="display-1">Store 1</h1>
                    <h1 class="display-1">Store 2</h1>
                    <h1 class="display-1">Store 3</h1>
                    <h1 class="display-1">Store 4</h1>
                    <h1 class="display-1">Store 5</h1>
                    <h1 class="display-1">Store 6</h1>
                    <h1 class="display-1">Store 7</h1>
                </div>
            </div>
        )
    }
}
//export Home Component
export default Home;