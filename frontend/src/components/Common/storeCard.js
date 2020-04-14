import React, { Component } from 'react';

class Home extends Component {

    render() {
        return(
            <div className="shadow row m-3 p-3 rounded border">
                <div className="col-md-8">
                    <h1 className="font-weight-lighter">Store Name</h1>
                    <h6 className="font-weight-lighter">1334 The Alameda Apt 388, San Jose, CA - 95126</h6>
                </div>
                <div className="col-md-4">
                    <img src="https://www.okea.org/wp-content/uploads/2019/10/placeholder.png" alt="..." class="img-thumbnail" />
                </div>
            </div>
        )
    }
}
export default Home;