import React, { Component } from 'react';

class Home extends Component {

    constructor() {
        super();
        this.state = {
            quantity: 0,
            errMsg: ""
        }
    }

    decreaseQuantity = () => {
        if (this.state.quantity > 0) {
            this.setState({
                quantity: this.state.quantity - 1
            })
        }
    }

    increaseQuantity = () => {
        this.setState({
            quantity: this.state.quantity + 1
        })
    }

    quantityChangeHandler = (e) => {
        if (e.target.value === "") {
            e.target.value = 0
        }
        if (isNaN(e.target.value) === false) {
            this.setState({
                quantity: parseInt(e.target.value, 10)
            })
        }
    }

    deleteProduct = () => {
        this.setState({
            errMsg: "Unable to delete this product"
        })
    }

    render() {

        let itemFunction = []
        if (this.props.isAdmin) {
            itemFunction = [
                <div className="row">
                    <div className="col-md-6">
                        <a href={`/admin/product/update/${this.props.storeId}/12039`}>
                            <button className="btn btn-warning w-100">Update this product</button>
                        </a>
                    </div>
                    <div className="col-md-6">
                        <button className="btn btn-danger w-100" onClick={this.deleteProduct}>Delete this product</button>
                    </div>
                </div>
            ]
        } else {
            itemFunction = [
                <div className="row">
                    <div className="col-md-1 p-0">
                        <button className="btn btn-danger w-100" onClick={this.decreaseQuantity}>-</button>
                    </div>
                    <div className="col-md-4 p-0">
                        <input type="text" className="form-control" value={this.state.quantity} onChange={this.quantityChangeHandler} />
                    </div>
                    <div className="col-md-1 p-0">
                        <button className="btn btn-success" onClick={this.increaseQuantity}>+</button>
                    </div>
                    <div className="col-md-5 offset-md-1">
                        <button className="btn btn-info">Add to cart</button>
                    </div>
                </div>
            ]
        }
        return (
            <div className="shadow row m-3 p-3 rounded border">
                <div className="col-md-8">
                    <h1 className="font-weight-lighter">Item Name</h1>
                    <h6 className="font-weight-lighter">This is description for the item</h6>
                    <h6>Brand: <span className="font-weight-lighter">Name of the brand</span></h6>
                    <h6>$12 / unit</h6>
                    <p className="text-danger">{this.state.errMsg}</p>
                    {itemFunction}
                </div>
                <div className="col-md-4">
                    <img src="https://www.okea.org/wp-content/uploads/2019/10/placeholder.png" alt="..." class="img-thumbnail" />
                </div>
            </div>
        )
    }
}
export default Home;