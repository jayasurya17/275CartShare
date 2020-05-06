import React, { Component } from 'react';
import Header from '../Common/header';
import Navigation from '../Common/navbar';
import axios from 'axios';

class Home extends Component {

    constructor() {
        super()
        this.state = {
            contributionCredit: null
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

    updateInfo = async () => {
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

        var str = this.state.nickName;
        for (i = 0, len = str.length; i < len; i++) {
            code = str.charCodeAt(i);
            if (!(code > 47 && code < 58) && // numeric (0-9)
                !(code > 64 && code < 91) && // upper alpha (A-Z)
                !(code > 96 && code < 123)) { // lower alpha (a-z)
                alert('The nickname can only contain alphanumeric characters');
                return;
            }
        }

        str = this.state.screenName;
        for (var i = 0, len = str.length; i < len; i++) {
            var code = str.charCodeAt(i);
            if (!(code > 47 && code < 58) && // numeric (0-9)
                !(code > 64 && code < 91) && // upper alpha (A-Z)
                !(code > 96 && code < 123)) { // lower alpha (a-z)
                alert('The screenname can only contain alphanumeric characters');
                return;
            }
        }


        if(this.state.zipcode.length !== 5){
            alert("Please enter a valid zipcode");
            return;
        }
        var response
        try {
            response = await axios.get(`https://api.mapbox.com/geocoding/v5/mapbox.places/${this.state.street}, ${this.state.state}, ${this.state.city}, ${this.state.zipcode}.json?access_token=pk.eyJ1Ijoic2hpdmFuZGVzYWkiLCJhIjoiY2syaW0xaXllMGcydTNjb2hua3UzbHpyMSJ9.ZHxE6FAsU4GeDvz4WH9AhA`)
        } catch (error) {
            alert('Please enter a valid address!');
            return;
        }
        
        if(response.data.features.length === 0){
            alert('Please enter a valid address!');
            return;
        }
        var isFound = false;
        for(var a of response.data.features){
            console.log('relevance', a.properties.accuracy);
            if (a.properties.accuracy !== undefined && a.relevance > 0.95){
                isFound = true
            } 
        }

        if(isFound === false){
            alert('Please enter a valid address!');
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


        let background
        if (this.state.contributionCredit <= -6) {
            background = "bg-danger"
        } else if (this.state.contributionCredit <= -4) {
            background = "bg-warning"
        } else if (this.state.contributionCredit != null) {
            background = "bg-success"
        }

        return (
            <div>
                <Header />
                <Navigation />
                <div className="pl-5 pr-5 row">
                    <div className="col-md-4 offset-md-4 pt-5">
                        <div className="form-group">
                            <label>NickName</label>
                            <br/>
                            <input type="text" value={this.state.nickName} onChange={this.nickNameChangeHandler} className="form-control" />
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