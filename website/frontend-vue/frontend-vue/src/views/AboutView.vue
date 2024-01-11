<template>
  <div class="about">
    <h1>This is an about page</h1>
    <MyButton label="Hello" />
  </div>
</template>

<script>
import MyButton from "@/components/MyButton.vue";

export default {
  name: "AboutView",
  components: {
    MyButton,
  },
};
</script>
