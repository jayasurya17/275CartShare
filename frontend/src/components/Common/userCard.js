import React, { Component } from 'react';

class Home extends Component {

    render() {
        
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