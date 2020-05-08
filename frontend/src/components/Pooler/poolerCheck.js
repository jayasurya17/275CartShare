import React, { Component } from 'react';
import { Redirect } from 'react-router';

class PoolerCheck extends Component {

    render() {
        let redirectTo = null
        if (localStorage.getItem('275UserId') === null || localStorage.getItem('275NickName') === null) {
            redirectTo = <Redirect to="/login" />
        } else if (localStorage.getItem('275UserType') === "Admin") {
            redirectTo = <Redirect to="/admin/browse/stores" />
        } else if (localStorage.getItem('isMember') === "true") {
            if (this.props.location.pathname === "/pooler/landing") {
                redirectTo = <Redirect to="/pooler/view/pool" />
            }
        } else if (localStorage.getItem('isMember') === "false") {
            if (this.props.location.pathname !== "/pooler/landing" && this.props.location.pathname !== "/pooler/browse" && this.props.location.pathname.startsWith("/pooler/store") === false) {
                redirectTo = <Redirect to="/pooler/landing" />
            }
        }

        return (
            <div>
                {redirectTo}
            </div>
        )
    }

}

export default PoolerCheck;