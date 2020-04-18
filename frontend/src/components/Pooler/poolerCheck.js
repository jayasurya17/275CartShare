import React, { Component } from 'react';
import { Redirect } from 'react-router';

class PoolerCheck extends Component {

    render() {
        let redirectTo = null
        if (localStorage.getItem('275UserId') === null) {
            redirectTo = <Redirect to="/login" />
        } else if (localStorage.getItem('275UserType') === "Admin") {
            redirectTo = <Redirect to="/admin/browse" />
        }
        return (
            <div>
                {redirectTo}
            </div>
        )
    }

}

export default PoolerCheck;