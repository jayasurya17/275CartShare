import React, { Component } from 'react';

class PoolCard extends Component {

    constructor() {
        super()
        this.state = {
            referenceName: "",
            knowsLeader: false,
            tempName: ""
        }
    }

    knowsLeader = () => {
        if (this.state.knowsLeader) {
            this.setState({
                referenceName: this.state.tempName,
                knowsLeader: false,
            })
        } else {
            this.setState({
                referenceName: "",
                knowsLeader: true
            })
        }
    }

    nameChangeHandler = (e) => {
        if (this.state.knowsLeader === false) {
            this.setState({
                referenceName: e.target.value,
                tempName: e.target.value
            })
        }
    }

    render() {
        return (
            <div className="border rounded m-2 p-3 bg-white row">
                <div className="col-md-9">
                    <h2>Pool name</h2>
                    <h4 className="font-weight-light">Description of the pool</h4>
                    <h4>Pool leader: <span className="font-weight-light">Name</span> </h4>
                    <h4>San Jose <span className="font-weight-light">- 95126</span></h4>
                </div>
                <div className="col-md-3">
                    <button className="btn btn-warning" data-toggle="modal" data-target="#modalCenter">Join this pool</button>
                </div>

                {/* <!-- Modal --> */}
                <div class="modal fade" id="modalCenter" tabindex="-1" role="dialog" aria-labelledby="modalCenterTitle" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="modalCenterTitle">Request to join poolname</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <div className="form-group">
                                    <label>Reffered by</label>
                                    <input type="text" className="form-control" value={this.state.referenceName} onChange={this.nameChangeHandler}/>
                                </div>
                                <p>(or)</p>
                                <input type="checkbox" onClick={this.knowsLeader} />
                                <label> I know the pool leader</label>
                                
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                <button type="button" class="btn btn-primary">Send request</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default PoolCard;