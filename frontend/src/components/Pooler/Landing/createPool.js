import React, { Component } from 'react';

class Home extends Component {

    render() {

        return (
            <div>
                <div className="form-group">
                    <label>Pool name</label>
                    <input type="text" className="form-control" />
                </div>
                <div className="form-group">
                    <label>Neighbourhood</label>
                    <input type="text" className="form-control" />
                </div>
                <div className="form-group">
                    <label>Description</label>
                    <input type="text" className="form-control" />
                </div>
                <div className="form-group">
                    <label>Zipcode</label>
                    <input type="text" className="form-control" />
                </div>
                <button className="btn btn-success w-100">Create a new pool</button>
            </div>
        )
    }
}
//export Home Component
export default Home;