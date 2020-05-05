import React, { Component } from 'react';
import axios from 'axios';

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
            errMsg: ""
        })
        axios.delete(`/admin/delete/store?storeId=${this.props.storeObj.id}`)
        .then(() => {
            this.props.getAllStores()
        })
        .catch(() => {
            this.setState({
                errMsg: "This store has unfulfilled orders. It cannot be deleted at the moment!"
            })
        })
    }

    setAsCurrentStore = (e) => {
        e.preventDefault();
        this.props.updateStore(this.props.storeObj.id);
        this.props.refresh(this.props.storeObj.id);
    }

    render() {

        let redirectTo = `/pooler/store/${this.props.storeObj.id}`
        let adminFunctions = []
        if (this.props.isAdmin) {
            redirectTo = `/admin/store/view/${this.props.storeObj.id}`
            adminFunctions = [
                <div className="row mt-4">
                    <div className="col-md-6 text-center">
                        <a href={`/admin/store/update/${this.props.storeObj.id}`}><button className="btn btn-warning">Update store details</button></a>
                    </div>
                    <div className="col-md-6 text-center">
                        <button className="btn btn-danger" onClick={this.deleteStore}>Delete this store</button>
                    </div>
                </div>
            ]
        } else if (this.props.setAsShopping) {
            adminFunctions = [
                <div className="row mt-4">
                    <div className="col-md-6 text-center">
                        <button className="btn btn-warning" onClick={this.setAsCurrentStore}>Shop from this store</button>
                    </div>
                </div>
            ]

        }

        let address = this.props.storeObj.address.street + ", " + this.props.storeObj.address.city + ", " + this.props.storeObj.address.state + " - " + this.props.storeObj.address.zipcode
        return (
            <a className="text-decoration-none text-dark" href={redirectTo}>
                <div className="shadow m-3 p-3 rounded border">
                    <div className="row">
                        <div className="col-md-12">
                            <h1 className="font-weight-lighter">{this.props.storeObj.storeName}</h1>
                            <h6 className="font-weight-lighter">{address}</h6>
                            <p className="text-danger">{this.state.errMsg}</p>
                        </div>
                    </div>
                    {adminFunctions}
                </div>

            </a>
        )
    }
}
export default Home;