import React, { Component } from 'react';
import Header from '../Common/header';
import Navigation from '../Common/navbar';

class Home extends Component {

    render() {

        return (
            <div>
                <Header />
                <Navigation />
                <div className="pl-5 pr-5 row">
                    <div className="col-md-6 offset-md-3 pt-5">
                        <div className="form-group">
                            <label>Name</label>
                            <input type="text" className="form-control" />
                        </div>
                        <div className="form-group">
                            <label>Email</label>
                            <input type="text" className="form-control" />
                        </div>
                        <div className="form-group">
                            <label>Password</label>
                            <input type="text" className="form-control" />
                        </div>
                        <button className="btn btn-success w-100">Update account</button>
                    </div>
                </div>
            </div>
        )
    }
}
//export Home Component
export default Home;