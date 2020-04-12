import React, {Component} from 'react';

class Home extends Component {

    render(){
        return(
            <div>
                <p className="display-1 text-center pt-5 mt-5">Welcome to CartShare</p>
                <h4 className="text-center mt-5 font-weight-light">Please check your email and verify before we can proceed</h4>
                

                {/* Use this if needed for verify by code */}
                <div className="row text-center mt-5">
                    <div className="col-md-4 offset-md-4">
                        <input type="text" className="form-control"/>
                    </div>
                </div>                
                <div className="row text-center mt-5">
                    <div className="col-md-4 offset-md-4">
                        <button className="btn btn-success w-50">Verify</button>
                    </div>
                </div>

            </div>
        )
    }
}
//export Home Component
export default Home;