import React, { Component } from 'react';

class Home extends Component {

    constructor() {
        super()
        this.state = {
            errMsg: ""
        }
    }

    deleteStore = (e) => {
        e.preventDefault();
        this.setState({
            errMsg: "This store has unfulfilled orders. It cannot be deleted at the moment!"
        })
    }

    render() {

        let redirectTo = "/pooler/store/id"
        let adminFunctions = []
        if (this.props.isAdmin) {
            redirectTo = "/admin/store/view/id"
            adminFunctions = [
                <div className="row mt-4">
                    <div className="col-md-6 text-center">
                        <a href="/admin/store/update/12"><button className="btn btn-warning">Update store details</button></a>
                    </div>
                    <div className="col-md-6 text-center">
                        <button className="btn btn-danger" onClick={this.deleteStore}>Delete this store</button>
                    </div>
                </div>
            ]
        }

        return (
            <a className="text-decoration-none text-dark" href={redirectTo}>
                <div className="shadow m-3 p-3 rounded border">
                    <div className="row">
                        <div className="col-md-8">
                            <h1 className="font-weight-lighter">Store Name</h1>
                            <h6 className="font-weight-lighter">1334 The Alameda Apt 388, San Jose, CA - 95126</h6>
                            <p className="text-danger">{this.state.errMsg}</p>
                        </div>
                        <div className="col-md-4">
                            <img src="https://www.okea.org/wp-content/uploads/2019/10/placeholder.png" alt="..." class="img-thumbnail" />
                        </div>
                    </div>
                    {adminFunctions}
                </div>

            </a>
        )
    }
}
export default Home;