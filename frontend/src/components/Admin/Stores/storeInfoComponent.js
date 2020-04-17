import React, { Component } from 'react';

class StoreInfoComponent extends Component {

    constructor() {
        super()
        this.state = {
            name: "",
            street: "",
            city: "",
            state: "",
            zipcode: "",
            selectedFile: "",
            filename: "",
            errMsg: "",
            successMsg: ""
        }
    }

    componentDidMount() {
        if (this.props.storeId) {
            this.setState({
                name: "Store Name",
                street: "100",
                city: "San Jose",
                state: "CA",
                zipcode: "95126",
            })
        }
    }

    nameChangeHandler = (e) => {
        this.setState({
            name: e.target.value
        })
    }

    streetChangeHandler = (e) => {
        this.setState({
            street: e.target.value
        })
    }

    cityChangeHandler = (e) => {
        this.setState({
            city: e.target.value
        })
    }

    stateChangeHandler = (e) => {
        this.setState({
            state: e.target.value
        })
    }

    zipcodeChangeHandler = (e) => {
        this.setState({
            zipcode: e.target.value
        })
    }

    fileUploadChangeHandler = (e) => {
        this.setState({
            selectedFile: e.target.files[0],
            filename: e.target.value
        });
    }

    createStore = () => {

    }

    updateStore = () => {

    }

    render() {
        let action = <button className="btn btn-success w-100" onClick={this.createStore}>Create store</button>
        if (this.props.storeId) {
            action = <button className="btn btn-warning w-100" onClick={this.updateStore}>Update store details</button>
        }

        return (
            <div className="pl-5 pr-5 row">
                <div className="col-md-6 offset-md-3 pt-5">
                    <div className="form-group">
                        <label>Name</label>
                        <input type="text" className="form-control" onChange={this.nameChangeHandler} value={this.state.name} />
                    </div>
                    <div className="form-group">
                        <label>Street</label>
                        <input type="text" className="form-control" onChange={this.streetChangeHandler} value={this.state.street} />
                    </div>
                    <div className="form-group">
                        <label>City</label>
                        <input type="text" className="form-control" onChange={this.cityChangeHandler} value={this.state.city} />
                    </div>
                    <div className="form-group">
                        <label>State</label>
                        <input type="text" className="form-control" onChange={this.stateChangeHandler} value={this.state.state} />
                    </div>
                    <div className="form-group">
                        <label>Zipcode</label>
                        <input type="text" className="form-control" onChange={this.zipcodeChangeHandler} value={this.state.zipcode} />
                    </div>
                    <div className="form-group">
                        <div className="form-text">Upload an image for this store</div>
                        <input type="file" onChange={this.fileUploadChangeHandler} value={this.state.filename} />
                    </div>
                    <p className="text-success text-center">{this.state.successMsg}</p>
                    <p className="text-danger text-center">{this.state.errMsg}</p>
                    {action}
                </div>
            </div>
        )
    }
}
//export StoreInfoComponent Component
export default StoreInfoComponent;