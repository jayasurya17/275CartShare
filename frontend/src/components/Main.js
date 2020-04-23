import React, { Component } from 'react';
import { Route } from 'react-router-dom';
import Landing from './Authentication/landing';
import Login from './Authentication/login';
import Logout from './Authentication/logout';
import VerifyUser from './Authentication/verify';
import UserDetails from './Authentication/userDetails';

import PoolerCheck from './Pooler/poolerCheck';
import PoolerLanding from './Pooler/Landing/landing';
import PoolerBrowseStores from './Pooler/browseStores';
import PoolerSearch from './Pooler/search';
import PoolerUpdateAccount from './Pooler/updateAccount';
import PoolerViewCart from './Pooler/ConfirmOrder/viewCart';

import AdminCheck from './Admin/adminCheck';
import AdminStores from './Admin/Stores/browseStores';
import AdminAddStore from './Admin/Stores/addStores';
import AdminViewStoreProducts from './Admin/Products/viewProducts';
import AdminUpdateStore from './Admin/Stores/updateStores';
import AdminAddProduct from './Admin/Products/addProducts';
import AdminUpdateProduct from './Admin/Products/updateProduct';



//Create a Main Component
class Main extends Component {
    render() {
        return (
            <div>
                {/*Render Different Component based on Route*/}
                <Route path="/" component={Landing} />
                <Route path="/login" component={Login} />
                <Route path="/logout" component={Logout} />
                <Route path="/verify" component={VerifyUser} />
                <Route path="/user-information" component={UserDetails} />

                <Route path="/pooler" component={PoolerCheck} />
                <Route path="/pooler/landing" component={PoolerLanding} />
                <Route path="/pooler/browse" component={PoolerBrowseStores} />
                <Route path="/pooler/search" component={PoolerSearch} />
                <Route path="/pooler/update/account" component={PoolerUpdateAccount} />
                <Route path="/pooler/view/cart" component={PoolerViewCart} />

                <Route path="/admin" component={AdminCheck} />
                <Route path="/admin/browse" component={AdminStores} />
                <Route path="/admin/store/add" component={AdminAddStore} />
                <Route path="/admin/store/view/:storeId" component={AdminViewStoreProducts} />
                <Route path="/admin/store/update/:storeId" component={AdminUpdateStore} />
                <Route path="/admin/product/add" component={AdminAddProduct} />
                <Route path="/admin/product/update/:storeId/:SKU" component={AdminUpdateProduct} />
            </div>
        )
    }
}
//Export The Main Component
export default Main;