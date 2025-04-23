<template>
  <div><<router-link >
    <router-view />
  </router-link></div>
  <div><<router-link to="/about">About</router-link></div>
  <div><<router-link to="/login">Login</router-link></div>
  <div><<router-link to="/register">Register</router-link></div>
  <div><<router-link to="/dashboard">Dashboard</router-link></div>
  <div><<router-link to="/profile">Profile</router-link></div>
  <div><<router-link to="/settings">Settings</router-link></div>
  </router-link></div>

</template>

<script>
export default {
  name: 'App',
  components: {
  }
}
</script>

<style>

</style>
