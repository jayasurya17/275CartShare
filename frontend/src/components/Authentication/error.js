import React, { Component } from 'react';

class ErrorPage extends Component {

    render() {

        return (
            <div className="row p-5 m-5">
                <div className="col-md-3 text-center">
                    <img src="/notFound.gif" className="img-thumbnail" alt="404" />
                </div>
                <div className="col-md-9">
                    <p className="display-2">The page you are looking for does not exist. Take me back <a href="/pooler/landing" className="text-decoration-none" style={{color: "#ff6600"}}>home</a></p>
                </div>
            </div>
        )
    }
}
//export ErrorPage Component
export default ErrorPage;