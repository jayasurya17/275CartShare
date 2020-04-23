import React, { Component } from 'react';
import Header from '../Common/header';
import Navigation from '../Common/navbar';

class Home extends Component {

    constructor() {
        super()
        this.state = {
            user: {
                contributionCredit: -10
            }
        }
    }

    render() {
        

        let background = "bg-success"
        if (this.state.user.contributionCredit <= -6 ) {
            background = "bg-danger"
        } else if (this.state.user.contributionCredit <= -4) {
            background = "bg-warning"
        }

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
                            <input type="password" className="form-control" />
                        </div>
                        <button className="btn btn-success w-100">Update account</button>
                        <div className="pt-5 row text-center">
                            <div className="col-md-8">Your contribution credit</div>
                            <div className="col-md-2 font-weight-bold">{this.state.user.contributionCredit}</div>
                        </div>
                        <div className="pt-2 row text-center">
                            <div className="col-md-8">Your contribution status</div>
                            <div className={`col-md-2 ${background}`}></div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}
//export Home Component
export default Home;