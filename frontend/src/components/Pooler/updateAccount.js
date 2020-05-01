import React, { Component } from 'react';
import Header from '../Common/header';
import Navigation from '../Common/navbar';
import firebase from 'firebase';
import axios from 'axios';

class Home extends Component {

    constructor() {
        super()
        this.state = {
            user: {
                contributionCredit: -10
            }
        }
    }

    componentWillMount = () => {

        var uri = '/user/'.concat(localStorage.getItem('275UserId'));
        axios.get(uri)
        .then((response) => {
            if(response.status === 200){
                console.log('resp', response.data);
                
                var dat = response.data;
                this.setState({
                    contributionCredit: dat.contributionCredit,
                    screenName: dat.screenName,
                    nickName: dat.nickName,
                    admin: dat.admin,
                    active: dat.active,
                    street: dat.address.street,
                    city: dat.address.city,
                    state: dat.address.state,
                    zipcode: dat.address.zipcode,
                    profileComplete: dat.profileComplete,
                    verified: dat.verified,
                    email: dat.email,

                })
            }
        })
        .catch((error) => {
            alert('problem getting data from backend');
        })
    }

    stateChangeHandler = (e) => {
        this.setState({
            state: e.target.value
        })
    }

    streetChangeHandler = (e) => {
        this.setState({
            street: e.target.value
        })
    }

    cityChangeHandler = (e) => {
        this.setState({
            city: e.target.value
        })
    }

    zipcodeChangeHandler = (e) => {
        this.setState({
            zipcode: e.target.value
        })
    }

    nickNameChangeHandler = (e) => {
        this.setState({
            nickName: e.target.value
        })
    }

    screenNameChangeHandler = (e) => {
        this.setState({
            screenName: e.target.value
        })
    }

    updateInfo = () => {
        if(this.state.nickName.length === 0 || this.state.screenName.length === 0 || this.state.street.length === 0 || this.state.city.length === 0 || this.state.state.length === 0 || this.state.zipcode.length === 0){
            alert("None of the fields should be empty");
            return;
        }
        if(this.state.nickName.localeCompare('notSet') === 0){
            alert("NickName is already taken");
            return;
        }
        if(this.state.screenName.localeCompare('notSet') === 0){
            alert("ScreenName is already taken");
            return;
        }

        if(this.state.zipcode.length !== 5){
            alert("Please enter a valid zipcode");
            return;
        }
        axios.put('/user/'.concat(localStorage.getItem('275UserId')), null, {
            params: {
                email: this.state.email,
                nickName: this.state.nickName,
                screenName: this.state.screenName,
                isAdmin: this.state.admin,
                isActive: this.state.active,
                isProfileComplete: this.state.profileComplete,
                isVerified: this.state.verified,
                city: this.state.city,
                street: this.state.street,
                state: this.state.state,
                zipcode: this.state.zipcode
            }
        })
        .then((resp) => {
            if(resp.status === 200){
                alert('user updated successfully')
            }
        })
        .catch((err) => {
            alert(err.response.data);
        })
    }

    render() {


        let background = "bg-success"
        if (this.state.user.contributionCredit <= -6) {
            background = "bg-danger"
        } else if (this.state.user.contributionCredit <= -4) {
            background = "bg-warning"
        }

        return (
            <div>
                <Header />
                <Navigation />
                <div className="pl-5 pr-5 row">
                    <div className="col-md-4 offset-md-1 pt-5">
                        <div className="form-group">
                            <label>NickName</label>
                            <br/>
                            <input type="text" value={this.state.nickName} onChange={this.nickNameChangeHandler} lassName="form-control" />
                        </div>
                        <div className="form-group">
                            <label>ScreenName</label>
                            <input type="text" value={this.state.screenName} onChange={this.screenNameChangeHandler} className="form-control" />
                        </div>
                        <div className="form-group">
                            <label>Street</label>
                            <input type="text" value={this.state.street} onChange={this.streetChangeHandler} className="form-control" />
                        </div>
                        <div className="form-group">
                            <label>City</label>
                            <input type="text" value={this.state.city} onChange={this.cityChangeHandler} className="form-control" />
                        </div>
                        <div className="form-group">
                            <label>State</label>
                            <input type="text" value={this.state.state} onChange={this.stateChangeHandler} className="form-control" />
                        </div>
                        <div className="form-group">
                            <label>Zipcode</label>
                            <input type="text" value={this.state.zipcode} onChange={this.zipcodeChangeHandler} className="form-control" />
                        </div>
                        <button className="btn btn-success w-100" onClick={this.updateInfo}>Update account</button>
                        <div className="pt-5 row text-center">
                            <div className="col-md-8">Your contribution credit</div>
                            <div className="col-md-2 font-weight-bold">{this.state.contributionCredit}</div>
                        </div>
                        <div className="pt-2 row text-center">
                            <div className="col-md-8">Your contribution status</div>
                            <div className={`col-md-2 ${background}`}></div>
                        </div>
                        <br/>
                        <br/>
                        <br/>
                    </div>
                </div>
            </div>
        )
    }
}
//export Home Component
export default Home;