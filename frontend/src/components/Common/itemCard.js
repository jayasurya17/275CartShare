import React, { Component } from 'react';

class Home extends Component {

    constructor() {
        super();
        this.state = {
            quantity: 0
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
        if (parseInt(e.target.value, 10) === e.target.value) {
            this.setState({
                quantity : e.target.value
            })
        }
    }

    render() {
        return(
            <div className="shadow row m-3 p-3 rounded border">
                <div className="col-md-8">
                    <h1 className="font-weight-lighter">Item Name</h1>
                    <h6 className="font-weight-lighter">This is description for the item</h6>
                    <h6>Brand: <span className="font-weight-lighter">Name of the brand</span></h6>
                    <h6>$12 / unit</h6>
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
                </div>
                <div className="col-md-4">
                    <img src="https://www.okea.org/wp-content/uploads/2019/10/placeholder.png" alt="..." class="img-thumbnail" />
                </div>
            </div>
        )
    }
}
export default Home;