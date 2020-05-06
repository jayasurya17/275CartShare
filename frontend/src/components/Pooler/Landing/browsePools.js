import React, { Component } from 'react'
import '../../../css/browsePool.css'
import PoolCard from './poolCard'
import axios from 'axios'

class Home extends Component {
  constructor (props) {
    super(props)
    this.state = {
      allPools: [],
      isFetched: false,
      searchPool: '',
      refreshSearch: []
    }
  }

  componentWillMount = () => {
    axios
      .get('/pool/getAllPools')
      .then(response => {
        if (response.status === 200) {
          console.log(response.data)
          this.setState({
            allPools: response.data,
            successMsg: 'All Pool Fetched',
            isFetched: true,
            refreshSearch: response.data
          })
        }
      })
      .catch(error => {
        this.setState({
          errMsg: 'No Pools Fetched',
          isFetched: true
        })
      })
  }
  searchTextChangeHandler = e => {
    this.setState({
      searchPool: e.target.value
    })
  }

  search = e => {
    e.preventDefault()

    if (this.state.searchPool) {
      let filteredPools = []
      //     x = this.state.allPools
      //   x.forEach(pool => {
      //     if (
      //       pool.zipcode === this.state.searchPool ||
      //       pool.neighborhoodName == this.state.searchPool
      //     )
      //       filteredPools.push({ pool })
      //   })
      //   console.log('filtered pools')
      //   console.log(filteredPools)
      //   console.log('all pools')
      //   console.log(this.state.allPools)
      for (var pool of this.state.allPools) {
        if (
          pool.zipcode === this.state.searchPool ||
          pool.neighborhoodName === this.state.searchPool
        ) {
          filteredPools.push(pool)
        }
      }
      this.setState({
        allPools: filteredPools
      })

      console.log('after pools')
      console.log(filteredPools)
    }
  }
  clearSearch = e => {
    this.setState({
      allPools: this.state.refreshSearch
    })
  }
  render () {
    if (this.state.isFetched === false) {
      return (
        <div>
          <input
            className='form-control'
            placeholder='Search by pool name or neighborhood name or zipcode'
          />
          <div className='scrollable'>
            <p>Fetching...</p>
          </div>
        </div>
      )
    }

    let poolCards
    if (this.state.allPools.length > 0) {
      console.log('render time')
      console.log(this.state.allPools)
      poolCards = this.state.allPools.map(pool => {
        return (
          <PoolCard
            poolName={pool.poolName}
            description={pool.description}
            leader={pool.pooler}
            zip={pool.zipcode}
            poolId={pool.id}
            alphaNumericId={pool.poolId}
            neighborhoodName={pool.neighborhoodName}
          />
        )
      })
    }

    return (
      <div>
        <input
          type='text'
          name='searchPool'
          className='form-control'
          onChange={this.searchTextChangeHandler}
          value={this.state.searchPool}
          placeholder='Search by pool name or neighborhood name or zipcode'
        />
        <input
          type='submit'
          className='form-control btn btn-warning w-100'
          placeholder='Search'
          value='Search'
          onClick={this.search}
        />
        <input
          type='button'
          className='form-control btn btn-info w-100'
          placeholder='Clear'
          value='Clear'
          onClick={this.clearSearch}
        />
        <div className='scrollable'>
          {poolCards ? poolCards : <p>No Pools Available</p>}
        </div>
      </div>
    )
  }
}

//export Home Component
export default Home
