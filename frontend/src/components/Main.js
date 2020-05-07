import React, { Component } from 'react'
import { Route, Switch } from 'react-router-dom'
import Landing from './Authentication/landing'
import Login from './Authentication/login'
import Logout from './Authentication/logout'
import VerifyUser from './Authentication/verify'
import UserDetails from './Authentication/userDetails'

import PoolerCheck from './Pooler/poolerCheck'
import PoolerLanding from './Pooler/Landing/landing'
import PoolerBrowseStores from './Pooler/Store/browseStores'
import PoolerViewStoreProducts from './Pooler/Store/viewProducts'
import PoolerSearch from './Pooler/search'
import PoolerUpdateAccount from './Pooler/updateAccount'
import PoolerAddCart from './Pooler/AddToCart/addToCart'
import PoolerViewCart from './Pooler/ConfirmOrder/viewCart'
import PoolerViewPool from './Pooler/Pool/viewDetails'
import PoolerPickupOrders from './Pooler/PickupOrders/pickupOrders'
import PoolerDeliverOrders from './Pooler/DeliverOrders/deliverOrders'
import PoolerPastOrders from './Pooler/PastOrders/pastOrders'

import AdminCheck from './Admin/adminCheck'
import AdminStores from './Admin/Stores/browseStores'
import AdminAddStore from './Admin/Stores/addStores'
import AdminViewStoreProducts from './Admin/Products/viewProducts'
import AdminUpdateStore from './Admin/Stores/updateStores'
import AdminAddProduct from './Admin/Products/addProducts'
import AdminUpdateProduct from './Admin/Products/updateProduct'
import AdminOrdersToBePickedUp from './Admin/OrdersToBePickedUp/ordersToBePickedUp'

import Messaging from './Messaging/messagingScreen'

import ErrorPage from './Authentication/error'

//Create a Main Component
class Main extends Component {
	render() {
		return (
			<div>
				{/*Render Different Component based on Route*/}
				<Route path='/' component={Landing} />
				<Route path='/pooler' component={PoolerCheck} />
				<Route path='/admin' component={AdminCheck} />

				<Switch>
					<Route path='/login' exact component={Login} />
					<Route path='/logout' exact component={Logout} />
					<Route path='/verify' exact component={VerifyUser} />
					<Route path='/user-information' exact component={UserDetails} />

					<Route path='/pooler/landing' exact component={PoolerLanding} />
					<Route path='/pooler/browse' exact component={PoolerBrowseStores} />
					<Route path='/pooler/store/:storeId' exact component={PoolerViewStoreProducts} />
					<Route path='/pooler/search' exact component={PoolerSearch} />
					<Route path='/pooler/update/account' exact component={PoolerUpdateAccount} />
					<Route path='/pooler/add/cart' exact component={PoolerAddCart} />
					<Route path='/pooler/view/cart' exact component={PoolerViewCart} />
					<Route path='/pooler/view/pool' exact component={PoolerViewPool} />
					<Route path='/pooler/pickup' exact component={PoolerPickupOrders} />
					<Route path='/pooler/deliver' exact component={PoolerDeliverOrders} />
					<Route path='/pooler/past/orders' exact component={PoolerPastOrders} />
					<Route path='/pooler/message' exact component={Messaging} />

					<Route path='/admin/browse' exact component={AdminStores} />
					<Route path='/admin/store/add' exact component={AdminAddStore} />
					<Route path='/admin/store/view/:storeId' exact component={AdminViewStoreProducts} />
					<Route path='/admin/store/update/:storeId' exact component={AdminUpdateStore} />
					<Route path='/admin/product/add' exact component={AdminAddProduct} />
					<Route path='/admin/product/update/:productId' exact component={AdminUpdateProduct} />
					<Route path='/admin/pickup/orders' exact component={AdminOrdersToBePickedUp} />

					<Route component={ErrorPage} />
				</Switch>
			</div>
		)
	}
}
//Export The Main Component
export default Main
