import React, { Component } from 'react'

class OrdersComponent extends Component {

    render() {
        let allProducts = []
        for (let product of this.props.order) {
            allProducts.push(
                <div className="row p-2">
                    <div className="col-md-3"><img src={product.productImage} alt="..." class="img-thumbnail" /></div>
                    <div className="col-md-4">{product.productName}</div>
                    <div className="col-md-3">{product.productBrand}</div>
                    <div className="col-md-2">{product.quantity}</div>
                </div>
            )
        }


        return (
            <div className="p-3 border">
                <div className="row p-2 bg-secondary text-white">
                    <div className="col-md-12">
                        <h5 className="font-weight-light">Order #{this.props.order[0].orders.id}</h5>
                    </div>
                </div>
                <div className="row p-2 bg-secondary text-white">
                    {/* <div className="col-md-3"><h5>Order: {this.props.slNo}</h5></div> */}
                    <div className="col-md-4 offset-md-3">Name</div>
                    <div className="col-md-3">Brand</div>
                    <div className="col-md-2">Quantity</div>
                </div>
                {allProducts}
            </div>
        )
    }
}

//export OrdersComponent Component
export default OrdersComponent;