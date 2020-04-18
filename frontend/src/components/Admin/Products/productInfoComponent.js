import React, { Component } from 'react';
import Select from 'react-select';

class StoreInfoComponent extends Component {

    constructor() {
        super()
        this.state = {
            SKU: "",
            name: "",
            description: "",
            brand: "",
            unit: "",
            price: "",
            errMsg: "",
            successMsg: "",
            selectedFile: "",
            filename: "",
            allStores: [],
            selectedStores: [],
            storeName: ""
        }
    }

    componentDidMount() {
        if (this.props.storeId) {
            this.setState({
                SKU: this.props.SKU,
                name: "Store Name",
                description: "A small description",
                brand: "B1",
                unit: "KG",
                price: "6",
                filename: "",
                storeName: "This is a name"
            })
        }
    }

    SKUChangeHandler = (e) => {
        this.setunit({
            SKU: e.target.value
        })
    }

    nameChangeHandler = (e) => {
        this.setunit({
            name: e.target.value
        })
    }

    descriptionChangeHandler = (e) => {
        this.setunit({
            description: e.target.value
        })
    }

    brandChangeHandler = (e) => {
        this.setunit({
            brand: e.target.value
        })
    }

    unitChangeHandler = (e) => {
        this.setunit({
            unit: e.target.value
        })
    }

    priceChangeHandler = (e) => {
        this.setunit({
            price: e.target.value
        })
    }

    onChangeMultiSelect = (opt) => {
        this.setState({
            selectedStores: opt
        });
    }

    fileUploadChangeHandler = (e) => {
        this.setState({
            selectedFile: e.target.files[0],
            filename: e.target.value
        });
    }

    addProduct = () => {

    }

    updateProduct = () => {

    }

    render() {
        let storeDetails = []
        if (this.props.storeId) {
            storeDetails = [
                <div className="form-group">
                    <label>Store name</label>
                    <input className="form-control" type="text" value={this.state.storeName} disabled />
                </div>
            ]
        } else {
            let allStores = [
                { label: "Name 1", value: "value 1" },
                { label: "Name 2", value: "value 2" }
            ]
            storeDetails = [
                <div className="form-group">
                    <label>Select Stores</label>
                    <Select isMulti onChange={this.onChangeMultiSelect} options={allStores} value={this.state.selectedStores} />
                </div>
            ]
        }
        let action = <button className="btn btn-success w-100" onClick={this.addProduct}>Add product</button>
        if (this.props.storeId && this.props.SKU) {
            action = <button className="btn btn-warning w-100" onClick={this.updateProduct}>Update product details</button>
        }

        return (
            <div className="pl-5 pr-5">
                <div className="row">
                    <div className="col-md-3 offset-md-2 pt-5">
                        <div className="form-group">
                            <label>SKU</label>
                            <input type="text" className="form-control" onChange={this.SKUChangeHandler} value={this.state.SKU} />
                        </div>
                        <div className="form-group">
                            <label>Name</label>
                            <input type="text" className="form-control" onChange={this.nameChangeHandler} value={this.state.name} />
                        </div>
                        <div className="form-group">
                            <label>Description</label>
                            <input type="text" className="form-control" onChange={this.descriptionChangeHandler} value={this.state.description} />
                        </div>
                        <div className="form-group">
                            <label>Brand</label>
                            <input type="text" className="form-control" onChange={this.brandChangeHandler} value={this.state.brand} />
                        </div>
                    </div>
                    <div className="col-md-3 offset-md-2 pt-5">
                        <div className="form-group">
                            <label>Unit</label>
                            <input type="text" className="form-control" onChange={this.unitChangeHandler} value={this.state.unit} />
                        </div>
                        <div className="form-group">
                            <label>Price</label>
                            <input type="text" className="form-control" onChange={this.priceChangeHandler} value={this.state.price} />
                        </div>
                        {storeDetails}
                        <div className="form-group">
                            <label>Upload an image for the product</label>
                            <input type="file" onChange={this.fileUploadChangeHandler} value={this.state.filename} />
                        </div>
                    </div>
                </div>
                <div className="row">
                    <div className="col-md-6 offset-md-3">
                        <p className="text-success text-center">{this.state.successMsg}</p>
                        <p className="text-danger text-center">{this.state.errMsg}</p>
                        {action}
                    </div>
                </div>
            </div>
        )
    }
}
//export StoreInfoComponent Component
export default StoreInfoComponent;