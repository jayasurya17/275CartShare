import React, { Component } from 'react';
import './header.css';

class Home extends Component {

    render() {

        return (
            <div className="row pl-3 pt-1 pb-1 stickyHeader bg-light text-secondary">
                <div className="col-md-8">
                    <h1 class="display-4">CartShare</h1>
                </div>
                <div className="col-md-2 offset-md-2">
                    <div className="row">
                        <div className="col-md-12 text-center">
                            <button className="btn btn-outline-success w-50">View Cart</button>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-md-12 text-center">
                            <p className="text-center">Not User? Logout</p>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}
//export Home Component
export default Home;