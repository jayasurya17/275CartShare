import React, { Component } from 'react';
import axios from 'axios';


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
            axios.get(`/store/details/${this.props.storeId}`)
                .then((response) => {
                    this.setState({
                        name: response.data.storeName,
                        street: response.data.address.street,
                        city: response.data.address.city,
                        state: response.data.address.state,
                        zipcode: response.data.address.zipcode,
                    })

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

    isEmpty = (value) => {
        if (value.trim().localeCompare("") === 0) {
            return true
        }
        return false
    }

    isValidZipCode = (zipcode) => {
        if (zipcode.length !== 5) {
            return false
        }
        for (var value of zipcode) {
            if (isNaN(parseInt(value, 10))) {
                return false
            }
        }
        return true
    }

    areValidValues = () => {
        if (this.isEmpty(this.state.name))
            return false
        if (this.isEmpty(this.state.street))
            return false
        if (this.isEmpty(this.state.city))
            return false
        if (this.isEmpty(this.state.state))
            return false
        if (this.isEmpty(this.state.zipcode))
            return false
        return true

    }

    createStore = async () => {
        if (this.isValidZipCode(this.state.zipcode) === false) {
            this.setState({
                successMsg: "",
                errMsg: "Invalid zipcode"
            })
        } else if (this.areValidValues()) {
            var response
            try {
                response = await axios.get(`https://api.mapbox.com/geocoding/v5/mapbox.places/${this.state.street}, ${this.state.state}, ${this.state.city}, ${this.state.zipcode}.json?access_token=pk.eyJ1Ijoic2hpdmFuZGVzYWkiLCJhIjoiY2syaW0xaXllMGcydTNjb2hua3UzbHpyMSJ9.ZHxE6FAsU4GeDvz4WH9AhA`)
            } catch (error) {
                alert('Please enter a valid address!');
                return;
            }
            if(response.data.features.length === 0){
                alert('Please enter a valid address!');
                return;
            }
            var isFound = false;
            for(var a of response.data.features){
                console.log('relevance', a.properties.accuracy);
                if (a.properties.accuracy !== undefined && a.relevance > 0.95){
                    isFound = true
                } 
            }

            if(isFound === false){
                alert('Please enter a valid address!');
                return;
            }

            const reqBody = {
                userId: localStorage.getItem('275UserId'),
                storeName: this.state.name,
                street: this.state.street,
                city: this.state.city,
                state: this.state.state,
                zipcode: this.state.zipcode
            }
            axios.post(`/admin/create/store`, reqBody)
                .then(() => {
                    this.setState({
                        errMsg: "",
                        successMsg: "Created",
                        name: "",
                        street: "",
                        city: "",
                        state: "",
                        zipcode: "",
                        selectedFile: "",
                        filename: "",
                    })
                    if (this.props.getAllStores) {
                        this.props.getAllStores()
                    }
                })
                .catch((error) => {
                    if (error.response) {
                        this.setState({
                            errMsg: error.response.data,
                            successMsg: ""
                        })
                    } else {
                        this.setState({
                            errMsg: "An error occured",
                            successMsg: ""
                        })
                    }
                })

        } else {
            this.setState({
                successMsg: "",
                errMsg: "Fields cannot be empty"
            })
        }
    }

    updateStore = async () => {
        if (this.isValidZipCode(this.state.zipcode) === false) {
            this.setState({
                successMsg: "",
                errMsg: "Invalid zipcode"
            })
        } else if (this.areValidValues()) {
            var response
            try {
                response = await axios.get(`https://api.mapbox.com/geocoding/v5/mapbox.places/${this.state.street}, ${this.state.state}, ${this.state.city}, ${this.state.zipcode}.json?access_token=pk.eyJ1Ijoic2hpdmFuZGVzYWkiLCJhIjoiY2syaW0xaXllMGcydTNjb2hua3UzbHpyMSJ9.ZHxE6FAsU4GeDvz4WH9AhA`)
            } catch (error) {
                alert('Please enter a valid address!');
                return;
            }

            if(response.data.features.length === 0){
                alert('Please enter a valid address!');
                return;
            }
            var isFound = false;
            for(var a of response.data.features){
                console.log('relevance', a.properties.accuracy);
                if (a.properties.accuracy !== undefined && a.relevance > 0.95){
                    isFound = true
                } 
            }

            if(isFound === false){
                alert('Please enter a valid address!');
                return;
            }
            
            const reqBody = {
                storeId: this.props.storeId,
                userId: localStorage.getItem('275UserId'),
                storeName: this.state.name,
                street: this.state.street,
                city: this.state.city,
                state: this.state.state,
                zipcode: this.state.zipcode
            }
            axios.put(`/admin/modify/store`, reqBody)
                .then(() => {
                    this.setState({
                        errMsg: "",
                        successMsg: "Updated"
                    })
                })
                .catch((error) => {
                    this.setState({
                        errMsg: error.response.data,
                        successMsg: ""
                    })
                })

        } else {
            this.setState({
                successMsg: "",
                errMsg: "Fields cannot be empty"
            })
        }
    }

    render() {
        let action = <button className="btn btn-success w-100 mb-5" onClick={this.createStore}>Create store</button>
        if (this.props.storeId) {
            action = <button className="btn btn-warning w-100 mb-5" onClick={this.updateStore}>Update store details</button>
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